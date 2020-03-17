// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import ai.verta.modeldb.versioning.blob.diff.Function3;
import ai.verta.modeldb.versioning.blob.visitors.Visitor;
import java.util.*;
import java.util.function.Function;

public class NotebookCodeBlob implements ProtoType {
  public PathDatasetComponentBlob Path;
  public GitCodeBlob GitRepo;

  public NotebookCodeBlob() {
    this.Path = null;
    this.GitRepo = null;
  }

  public Boolean isEmpty() {
    if (this.Path != null) {
      return false;
    }
    if (this.GitRepo != null) {
      return false;
    }
    return true;
  }

  // TODO: not consider order on lists
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof NotebookCodeBlob)) return false;
    NotebookCodeBlob other = (NotebookCodeBlob) o;

    {
      Function3<PathDatasetComponentBlob, PathDatasetComponentBlob, Boolean> f =
          (x, y) -> x.equals(y);
      if (this.Path != null || other.Path != null) {
        if (this.Path == null && other.Path != null) return false;
        if (this.Path != null && other.Path == null) return false;
        if (!f.apply(this.Path, other.Path)) return false;
      }
    }
    {
      Function3<GitCodeBlob, GitCodeBlob, Boolean> f = (x, y) -> x.equals(y);
      if (this.GitRepo != null || other.GitRepo != null) {
        if (this.GitRepo == null && other.GitRepo != null) return false;
        if (this.GitRepo != null && other.GitRepo == null) return false;
        if (!f.apply(this.GitRepo, other.GitRepo)) return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.Path, this.GitRepo);
  }

  public NotebookCodeBlob setPath(PathDatasetComponentBlob value) {
    this.Path = value;
    return this;
  }

  public NotebookCodeBlob setGitRepo(GitCodeBlob value) {
    this.GitRepo = value;
    return this;
  }

  public static NotebookCodeBlob fromProto(ai.verta.modeldb.versioning.NotebookCodeBlob blob) {
    if (blob == null) {
      return null;
    }

    NotebookCodeBlob obj = new NotebookCodeBlob();
    {
      Function<ai.verta.modeldb.versioning.NotebookCodeBlob, PathDatasetComponentBlob> f =
          x -> PathDatasetComponentBlob.fromProto(blob.getPath());
      obj.Path = Utils.removeEmpty(f.apply(blob));
    }
    {
      Function<ai.verta.modeldb.versioning.NotebookCodeBlob, GitCodeBlob> f =
          x -> GitCodeBlob.fromProto(blob.getGitRepo());
      obj.GitRepo = Utils.removeEmpty(f.apply(blob));
    }
    return obj;
  }

  public ai.verta.modeldb.versioning.NotebookCodeBlob.Builder toProto() {
    ai.verta.modeldb.versioning.NotebookCodeBlob.Builder builder =
        ai.verta.modeldb.versioning.NotebookCodeBlob.newBuilder();
    {
      if (this.Path != null) {
        Function<ai.verta.modeldb.versioning.NotebookCodeBlob.Builder, Void> f =
            x -> {
              builder.setPath(this.Path.toProto());
              return null;
            };
        f.apply(builder);
      }
    }
    {
      if (this.GitRepo != null) {
        Function<ai.verta.modeldb.versioning.NotebookCodeBlob.Builder, Void> f =
            x -> {
              builder.setGitRepo(this.GitRepo.toProto());
              return null;
            };
        f.apply(builder);
      }
    }
    return builder;
  }

  public void preVisitShallow(Visitor visitor) throws ModelDBException {
    visitor.preVisitNotebookCodeBlob(this);
  }

  public void preVisitDeep(Visitor visitor) throws ModelDBException {
    this.preVisitShallow(visitor);
    visitor.preVisitDeepPathDatasetComponentBlob(this.Path);
    visitor.preVisitDeepGitCodeBlob(this.GitRepo);
  }

  public NotebookCodeBlob postVisitShallow(Visitor visitor) throws ModelDBException {
    return visitor.postVisitNotebookCodeBlob(this);
  }

  public NotebookCodeBlob postVisitDeep(Visitor visitor) throws ModelDBException {
    this.Path = visitor.postVisitDeepPathDatasetComponentBlob(this.Path);
    this.GitRepo = visitor.postVisitDeepGitCodeBlob(this.GitRepo);
    return this.postVisitShallow(visitor);
  }
}