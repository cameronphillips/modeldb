// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.visitors.Visitor;

public class NotebookCodeBlob {
    public PathDatasetBlob Path;
    public GitCodeBlob GitRepo;

    public NotebookCodeBlob() {
        this.Path = null;
        this.GitRepo = null;
    }

    public NotebookCodeBlob setPath(PathDatasetBlob value) {
        this.Path = value;
        return this;
    }
    public NotebookCodeBlob setGitRepo(GitCodeBlob value) {
        this.GitRepo = value;
        return this;
    }

    static public NotebookCodeBlob fromProto(ai.verta.modeldb.versioning.NotebookCodeBlob blob) {
        NotebookCodeBlob obj = new NotebookCodeBlob();
        {
            Function<Void,PathDatasetBlob> f = x -> PathDatasetBlob.fromProto(blob.getPath());
            if (f != null) {
                obj.Path = f.apply(null);
            }
        }
        {
            Function<Void,GitCodeBlob> f = x -> GitCodeBlob.fromProto(blob.getGitRepo());
            if (f != null) {
                obj.GitRepo = f.apply(null);
            }
        }
        return obj;
    }

    public void preVisitShallow(Visitor visitor) throws ModelDBException {
        visitor.preVisitNotebookCodeBlob(this);
    }

    public void preVisitDeep(Visitor visitor) throws ModelDBException {
        this.preVisitShallow(visitor);
        visitor.preVisitDeepPathDatasetBlob(this.Path);
        visitor.preVisitDeepGitCodeBlob(this.GitRepo);
    }

    public NotebookCodeBlob postVisitShallow(Visitor visitor) throws ModelDBException {
        return visitor.postVisitNotebookCodeBlob(this);
    }

    public NotebookCodeBlob postVisitDeep(Visitor visitor) throws ModelDBException {
        this.Path = visitor.postVisitDeepPathDatasetBlob(this.Path);
        this.GitRepo = visitor.postVisitDeepGitCodeBlob(this.GitRepo);
        return this.postVisitShallow(visitor);
    }
}
