import * as React from 'react';

import { IObservation, Observation } from 'shared/models/Observation';
import { withScientificNotationOrRounded } from 'shared/utils/formatters/number';
import ScrollableContainer from 'shared/view/elements/ScrollableContainer/ScrollableContainer';

import ClientSuggestion from '../../shared/ClientSuggestion/ClientSuggestion';
import ObservationButton from '../ObservationButton/ObservationButton';

interface ILocalProps {
  observations: IObservation[];
  getObservationClassname?: (key: string) => string | undefined;
  maxHeight?: number;
  docLink?: string;
}

class Observations extends React.PureComponent<ILocalProps> {
  public render() {
    const {
      observations,
      getObservationClassname = () => undefined,
      docLink,
      maxHeight,
    } = this.props;
    const groupedObs = groupObservations(observations);
    const observationKeys = [...groupedObs.keys()];
    return groupedObs.size !== 0 ? (
      <ScrollableContainer
        maxHeight={maxHeight || 180}
        containerOffsetValue={12}
        children={
          <>
            {observationKeys.map((attributeKey: string, i: number) => (
              <ObservationButton
                additionalClassname={getObservationClassname(attributeKey)}
                groupedObs={groupedObs}
                attributeKey={attributeKey}
                key={i}
              />
            ))}
          </>
        }
      />
    ) : docLink ? (
      <ClientSuggestion
        fieldName={'observation'}
        clientMethod={'log_observation()'}
        link={docLink}
      />
    ) : (
      ''
    );
  }
}

export interface IObservationsValues {
  value: string | number;
  timeStamp: Date;
  epochNumber?: number;
}

export const groupObservations = (observations: Observation[]) => {
  const map: Map<string, IObservationsValues[]> = new Map();
  observations.forEach((obs: Observation) => {
    const key = obs.attribute.key;
    const collection = map.get(key);
    if (!collection) {
      if (obs.attribute.key) {
        map.set(key, [
          {
            value:
              typeof obs.attribute.value === 'number'
                ? withScientificNotationOrRounded(Number(obs.attribute.value))
                : obs.attribute.value,
            timeStamp: obs.timestamp,
            epochNumber: obs.epochNumber,
          },
        ]);
      }
    } else {
      if (obs.attribute.key) {
        collection.push({
          value:
            typeof obs.attribute.value === 'number'
              ? withScientificNotationOrRounded(Number(obs.attribute.value))
              : obs.attribute.value,
          timeStamp: obs.timestamp,
          epochNumber: obs.epochNumber,
        });
      }
    }
  });

  return map;
};

export default Observations;
