package ai.verta.modeldb.versioning.blob.visitors;

import static org.junit.Assert.fail;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.BlobDiff;
import ai.verta.modeldb.versioning.CodeDiff;
import ai.verta.modeldb.versioning.ConfigDiff;
import ai.verta.modeldb.versioning.DatasetDiff;
import ai.verta.modeldb.versioning.DiffStatusEnum.DiffStatus;
import ai.verta.modeldb.versioning.EnvironmentDiff;
import ai.verta.modeldb.versioning.EnvironmentVariablesBlob;
import ai.verta.modeldb.versioning.EnvironmentVariablesDiff;
import ai.verta.modeldb.versioning.GitCodeBlob;
import ai.verta.modeldb.versioning.GitCodeDiff;
import ai.verta.modeldb.versioning.HyperparameterConfigBlob;
import ai.verta.modeldb.versioning.HyperparameterConfigDiff;
import ai.verta.modeldb.versioning.HyperparameterValuesConfigBlob;
import ai.verta.modeldb.versioning.PathDatasetComponentBlob;
import ai.verta.modeldb.versioning.S3DatasetComponentBlob;
import ai.verta.modeldb.versioning.S3DatasetComponentDiff;
import ai.verta.modeldb.versioning.S3DatasetDiff;
import ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model.AutogenBlobDiff;
import io.grpc.Status.Code;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
* Examples:
*
location: "location"
config {
  hyperparameters {
    B {
      name: "test"
      value {
        string_value: "test"
      }
    }
  }
}

code {
  git {
    status: ADDED
    B {
      repo: "test"
    }
  }
}

location: "location"
status: MODIFIED
environment {
  environment_variables {
    status: ADDED
    A {
      name: "word1 word2"
    }
    B {
      name: "word1"
    }
  }
}

location: "location"
status: DELETED
dataset {
  s3 {
    components {
      path {
        status: DELETED
        A {
          md5: "test_md5"
        }
      }
    }
  }
}
*/
@RunWith(Parameterized.class)
public class ValidatorBlobDiffTest {
  private static final Logger LOGGER = LogManager.getLogger(ValidatorBlobDiffTest.class);

  private static final Validator validator = new Validator();
  private static final BlobDiff.Builder blobDiffs[] = {
    BlobDiff.newBuilder(),
    BlobDiff.newBuilder()
        .addLocation("location")
        .setConfig(
            ConfigDiff.newBuilder()
                .addHyperparameters(
                    HyperparameterConfigDiff.newBuilder()
                        .setB(
                            HyperparameterConfigBlob.newBuilder()
                                .setName("test")
                                .setValue(
                                    HyperparameterValuesConfigBlob.newBuilder()
                                        .setStringValue("test"))))
                .build()),
    BlobDiff.newBuilder()
        .setCode(
            CodeDiff.newBuilder()
                .setGit(
                    GitCodeDiff.newBuilder()
                        .setStatus(DiffStatus.ADDED)
                        .setB(GitCodeBlob.newBuilder().setRepo("test")))),
    BlobDiff.newBuilder()
        .addLocation("location")
        .setStatus(DiffStatus.MODIFIED)
        .setEnvironment(
            EnvironmentDiff.newBuilder()
                .addEnvironmentVariables(
                    EnvironmentVariablesDiff.newBuilder()
                        .setStatus(DiffStatus.ADDED)
                        .setA(EnvironmentVariablesBlob.newBuilder().setName("word1 word2"))
                        .setB(EnvironmentVariablesBlob.newBuilder().setName("word1")))),
    BlobDiff.newBuilder()
        .addLocation("location")
        .setStatus(DiffStatus.DELETED)
        .setDataset(
            DatasetDiff.newBuilder()
                .setS3(
                    S3DatasetDiff.newBuilder()
                        .addComponents(
                            S3DatasetComponentDiff.newBuilder()
                                .setA(
                                    S3DatasetComponentBlob.newBuilder()
                                        .setPath(
                                            PathDatasetComponentBlob.newBuilder()
                                                .setMd5("test_md5")))
                                .setStatus(DiffStatus.DELETED))))
  };

  private final BlobDiff blobDiff;

  @Parameters
  public static Collection<Object[]> data() {
    List<Object[]> result = new LinkedList<>();
    for (BlobDiff.Builder b : blobDiffs) {
      result.add(new Object[] {b.build()});
    }
    return result;
  }

  public ValidatorBlobDiffTest(BlobDiff blobDiff) {
    this.blobDiff = blobDiff;
  }

  @Test
  public void testValidateBlobDiff() {
    try {
      validator.validate(AutogenBlobDiff.fromProto(blobDiff));
      fail("Expected exception has not occurred");
    } catch (ModelDBException e) {
      LOGGER.info(e.getMessage());
      Assert.assertEquals(Code.INVALID_ARGUMENT, e.getCode());
    }
  }
}
