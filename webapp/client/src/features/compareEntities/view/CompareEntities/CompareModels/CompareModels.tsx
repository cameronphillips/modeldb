import { bind } from 'decko';
import * as React from 'react';
import { connect } from 'react-redux';

import Artifacts from 'features/artifactManager/view/Artifacts/Artifacts';
import Observations from 'shared/view/domain/ModelRecord/ModelRecordProps/Observations/Observations/Observations';
import Parameters from 'shared/view/domain/ModelRecord/ModelRecordProps/Parameters/Parameters';
import ClientSuggestion from 'shared/view/domain/ModelRecord/ModelRecordProps/shared/ClientSuggestion/ClientSuggestion';
import vertaDocLinks from 'shared/utils/globalConstants/vertaDocLinks';
import withProps from 'shared/utils/react/withProps';
import IdView from 'shared/view/elements/IdView/IdView';
import Preloader from 'shared/view/elements/Preloader/Preloader';
import TagBlock from 'shared/view/elements/TagBlock/TagBlock';
import ModelRecord from 'shared/models/ModelRecord';
import { Project } from 'shared/models/Project';
import {
  ComparedEntityIds,
  IModelsDifferentProps,
  selectModelsDifferentProps,
  ComparedModels,
  selectComparedModels,
  EntityType,
  IModelRecordView,
} from 'features/compareEntities/store';
import {
  loadExperimentRun,
  selectIsLoadingExperimentRun,
} from 'features/experimentRuns/store';
import { selectProject } from 'features/projects/store';
import { IApplicationState, IConnectedReduxProps } from 'setup/store/store';

import ComparableAttributes from '../shared/ComparableAttributes/ComparableAttributes';
import {
  getDiffValueBgClassname,
  getDiffValueBorderClassname,
} from '../shared/DiffHighlight/DiffHighlight';
import ComparableCodeVersion from './ComparableCodeVersion/ComparableCodeVersion';
import ComparableDatasets from './ComparableDatasets/ComparableDatasets';
import styles from './CompareModels.module.css';
import CompareModelsTable, {
  PropDefinition,
  IPropDefinitionRenderProps,
} from './CompareModelsTable/CompareModelsTable';
import { NA } from 'shared/view/elements/PageComponents';
import sortArrayByAnotherArrayKeys from 'features/versioning/compareCommits/view/DiffView/shared/sortArrayByAnotherArrayKeys/sortArrayByAnotherArrayKeys';
import Reloading from 'shared/view/elements/Reloading/Reloading';

interface ILocalProps {
  comparedModelIds: Required<ComparedEntityIds>;
  projectId: string;
}

interface IPropsFromState {
  modelsDifferentProps: IModelsDifferentProps | undefined;
  comparedModels: ComparedModels;
  isLoadingComparedModels: boolean;
  project: Project | undefined;
}

type AllProps = ILocalProps & IPropsFromState & IConnectedReduxProps;

class CompareModels extends React.PureComponent<AllProps> {
  public componentDidMount() {
    this.loadModels();
  }

  public render() {
    const {
      modelsDifferentProps,
      comparedModels,
      project,
      isLoadingComparedModels,
    } = this.props;

    return (
      <div className={styles.root}>
        <Reloading onReload={this.loadModels}>
          {isLoadingComparedModels ||
          comparedModels.length !== 2 ||
          !modelsDifferentProps ? (
            <Preloader variant="dots" />
          ) : (
            <div className={styles.content}>
              <div className={'compare_table'}>
                <CompareModelsTable
                  models={getComparedModelsView(comparedModels as Required<
                    ComparedModels
                  >)}
                  modelsDifferentProps={modelsDifferentProps}
                  columns={{
                    [EntityType.entity1]: { title: 'Model 1' },
                    [EntityType.entity2]: { title: 'Model 2' },
                  }}
                >
                  <PropDefinition
                    prop="id"
                    title="ID"
                    getValue={modelRecord => modelRecord.id}
                    render={withProps(SingleValue)({ propertyType: 'id' })}
                  />
                  <PropDefinition
                    prop="ownerId"
                    title="Owner"
                    getValue={modelRecord => modelRecord.owner.username}
                    render={withProps(SingleValue)({ propertyType: 'ownerId' })}
                  />
                  <PropDefinition
                    prop="experimentId"
                    title="Experiment"
                    getValue={modelRecord => modelRecord.shortExperiment.name}
                    render={withProps(SingleValue)({
                      propertyType: 'experimentId',
                    })}
                  />
                  <PropDefinition
                    prop="projectId"
                    title="Project"
                    getValue={_ => (project ? project.name : '')}
                    render={withProps(SingleValue)({
                      propertyType: 'projectId',
                    })}
                  />
                  <PropDefinition
                    prop="tags"
                    title="Tags"
                    getValue={model => model.tags}
                    render={({ currentEntityInfo: { value } }) =>
                      value.length !== 0 ? (
                        <TagBlock tags={value} />
                      ) : (
                        <ClientSuggestion
                          fieldName={'tags'}
                          clientMethod={'log_tags()'}
                          link={vertaDocLinks.log_tags}
                        />
                      )
                    }
                  />
                  <PropDefinition
                    prop="hyperparameters"
                    title="Hyperparameters"
                    getValue={model => model.hyperparameters}
                    render={({
                      currentEntityInfo: { value, entityType: modelType },
                      diffInfo: differentValues,
                    }) => (
                      <Parameters
                        prop="hyperparameters"
                        parameters={value}
                        getValueClassname={key =>
                          getDiffValueBgClassname(
                            modelType,
                            differentValues[key]
                          )
                        }
                      />
                    )}
                  />
                  <PropDefinition
                    prop="metrics"
                    title="Metrics"
                    getValue={model => model.metrics}
                    render={({
                      currentEntityInfo: { value, entityType: modelType },
                      diffInfo: differentValues,
                    }) => (
                      <Parameters
                        prop="metrics"
                        parameters={value}
                        getValueClassname={key =>
                          getDiffValueBgClassname(
                            modelType,
                            differentValues[key]
                          )
                        }
                      />
                    )}
                  />
                  <PropDefinition
                    prop="attributes"
                    title="Attributes"
                    getValue={model => model.attributes}
                    render={({
                      currentEntityInfo: { value, entityType: modelType },
                      diffInfo,
                    }) => {
                      const [model1, model2] = comparedModels as [
                        ModelRecord,
                        ModelRecord
                      ];
                      return (
                        <ComparableAttributes
                          entityType={modelType}
                          entity1Attributes={model1.attributes}
                          entity2Attributes={model2.attributes}
                          diffInfo={diffInfo}
                          docLink={vertaDocLinks.log_attribute}
                        />
                      );
                    }}
                  />
                  <PropDefinition
                    prop="artifacts"
                    title="Artifacts"
                    getValue={model => model.artifacts}
                    render={({
                      currentEntityInfo: {
                        value,
                        entityType: modelType,
                        model,
                      },
                      diffInfo: differentValues,
                    }) => (
                      <Artifacts
                        entityType="experimentRun"
                        entitiyId={model.id}
                        artifacts={value}
                        docLink={vertaDocLinks.log_artifact}
                        getArtifactClassname={key =>
                          getDiffValueBorderClassname(
                            modelType,
                            differentValues[key]
                          )
                        }
                      />
                    )}
                  />
                  <PropDefinition
                    prop="datasets"
                    title="Datasets"
                    getValue={model => model.datasets}
                    render={({
                      currentEntityInfo: { entityType: modelType },
                      diffInfo: differentValues,
                    }) => (
                      <ComparableDatasets
                        entity1Datasets={comparedModels[0]!.datasets}
                        entity2Datasets={comparedModels[1]!.datasets}
                        diffInfo={differentValues}
                        entityType={modelType}
                        docLink={vertaDocLinks.log_dataset}
                      />
                    )}
                  />
                  <PropDefinition
                    prop="observations"
                    title="Observations"
                    getValue={model => model.observations}
                    render={({ currentEntityInfo: { value } }) => (
                      <Observations
                        observations={value}
                        docLink={vertaDocLinks.log_observations}
                      />
                    )}
                  />
                  <PropDefinition
                    prop="codeVersion"
                    title="Code Version"
                    getValue={model => model.codeVersion}
                    render={({
                      currentEntityInfo,
                      otherEntityInfo,
                      diffInfo,
                    }) => {
                      if (
                        !currentEntityInfo.value.usual &&
                        !currentEntityInfo.value.fromBlobs
                      ) {
                        return NA;
                      }
                      return (
                        <>
                          {currentEntityInfo.value.usual && (
                            <ComparableCodeVersion
                              currentEntityInfo={{
                                codeVersion: currentEntityInfo.value.usual,
                                entityType: currentEntityInfo.entityType,
                                id: currentEntityInfo.model.id,
                              }}
                              otherEntityInfo={{
                                codeVersion: otherEntityInfo.value.usual,
                                entityType: otherEntityInfo.entityType,
                                id: otherEntityInfo.model.id,
                              }}
                              diffInfo={diffInfo.usual}
                            />
                          )}
                          {(() => {
                            const diffFromBlobs = diffInfo.fromBlobs;
                            if (
                              !currentEntityInfo.value.fromBlobs ||
                              !diffFromBlobs
                            ) {
                              return null;
                            }
                            const currentEntityCodeVersions = Object.entries(
                              currentEntityInfo.value.fromBlobs
                            );
                            const otherEntityCodeVersions = otherEntityInfo
                              .value.fromBlobs
                              ? Object.entries(otherEntityInfo.value.fromBlobs)
                              : [];
                            return sortArrayByAnotherArrayKeys(
                              ([location]) => location,
                              currentEntityCodeVersions,
                              otherEntityCodeVersions
                            ).map(([location, codeVersion]) => {
                              const currentCodeVersionDiffInfo =
                                diffFromBlobs[location];
                              return (
                                <ComparableCodeVersion
                                  currentEntityInfo={{
                                    codeVersion,
                                    entityType: currentEntityInfo.entityType,
                                    id: currentEntityInfo.model.id,
                                    label: location,
                                  }}
                                  otherEntityInfo={{
                                    codeVersion: otherEntityInfo.value.fromBlobs
                                      ? otherEntityInfo.value.fromBlobs[
                                          location
                                        ]
                                      : undefined,
                                    entityType: otherEntityInfo.entityType,
                                    id: otherEntityInfo.model.id,
                                    label: location,
                                  }}
                                  diffInfo={currentCodeVersionDiffInfo}
                                />
                              );
                            });
                          })()}
                        </>
                      );
                    }}
                  />
                </CompareModelsTable>
              </div>
            </div>
          )}
        </Reloading>
      </div>
    );
  }

  @bind
  private loadModels() {
    const { dispatch, comparedModelIds } = this.props;
    dispatch(loadExperimentRun('', comparedModelIds[0]));
    dispatch(loadExperimentRun('', comparedModelIds[1]));
  }
}

type SingleValuePropertyType = 'id' | 'experimentId' | 'projectId' | 'ownerId';
function SingleValue<
  Props extends IPropDefinitionRenderProps<SingleValuePropertyType>
>({
  currentEntityInfo: { value, model, entityType: modelType },
  diffInfo: isDifferent,
  propertyType,
}: Props & { propertyType: SingleValuePropertyType }) {
  return (
    <span
      className={getDiffValueBgClassname(modelType, isDifferent)}
      data-test={`property-value-${propertyType}`}
    >
      {propertyType === 'id' ? <IdView value={value} /> : value}
    </span>
  );
}

const mapStateToProps = (
  state: IApplicationState,
  localProps: ILocalProps
): IPropsFromState => {
  return {
    modelsDifferentProps: selectModelsDifferentProps(
      state,
      localProps.projectId,
      localProps.comparedModelIds
    ),
    comparedModels: selectComparedModels(
      state,
      localProps.projectId,
      localProps.comparedModelIds
    ),
    project: selectProject(state, localProps.projectId),
    isLoadingComparedModels:
      selectIsLoadingExperimentRun(state, localProps.comparedModelIds[0]) ||
      selectIsLoadingExperimentRun(state, localProps.comparedModelIds[1]),
  };
};

const getComparedModelsView = ([modelRecord1, modelRecord2]: Required<
  ComparedModels
>): [IModelRecordView, IModelRecordView] => {
  return [
    {
      ...modelRecord1,
      codeVersion: {
        usual: modelRecord1.codeVersion,
        fromBlobs: modelRecord1.codeVersionsFromBlob,
      },
    },
    {
      ...modelRecord2,
      codeVersion: {
        usual: modelRecord2.codeVersion,
        fromBlobs: modelRecord2.codeVersionsFromBlob,
      },
    },
  ];
};

export type ICompareModelsLocalProps = ILocalProps;
export default connect(mapStateToProps)(CompareModels);
