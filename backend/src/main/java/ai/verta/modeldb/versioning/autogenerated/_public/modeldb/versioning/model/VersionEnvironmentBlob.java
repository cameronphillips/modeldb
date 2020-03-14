// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.visitors.Visitor;

public class VersionEnvironmentBlob {
    public Integer Major;
    public Integer Minor;
    public Integer Patch;
    public String Suffix;

    public VersionEnvironmentBlob() {
        this.Major = 0;
        this.Minor = 0;
        this.Patch = 0;
        this.Suffix = null;
    }

    public VersionEnvironmentBlob setMajor(Integer value) {
        this.Major = value;
        return this;
    }
    public VersionEnvironmentBlob setMinor(Integer value) {
        this.Minor = value;
        return this;
    }
    public VersionEnvironmentBlob setPatch(Integer value) {
        this.Patch = value;
        return this;
    }
    public VersionEnvironmentBlob setSuffix(String value) {
        this.Suffix = value;
        return this;
    }

    static public VersionEnvironmentBlob fromProto(ai.verta.modeldb.versioning.VersionEnvironmentBlob blob) {
        VersionEnvironmentBlob obj = new VersionEnvironmentBlob();
        {
            Function<Integer,Integer> f = null;
            if (f != null) {
                obj.Major = f.apply(null);
            }
        }
        {
            Function<Integer,Integer> f = null;
            if (f != null) {
                obj.Minor = f.apply(null);
            }
        }
        {
            Function<Integer,Integer> f = null;
            if (f != null) {
                obj.Patch = f.apply(null);
            }
        }
        {
            Function<String,String> f = null;
            if (f != null) {
                obj.Suffix = f.apply(null);
            }
        }
        return obj;
    }

    public void preVisitShallow(Visitor visitor) throws ModelDBException {
        visitor.preVisitVersionEnvironmentBlob(this);
    }

    public void preVisitDeep(Visitor visitor) throws ModelDBException {
        this.preVisitShallow(visitor);
        visitor.preVisitDeepInteger(this.Major);
        visitor.preVisitDeepInteger(this.Minor);
        visitor.preVisitDeepInteger(this.Patch);
        visitor.preVisitDeepString(this.Suffix);
    }

    public VersionEnvironmentBlob postVisitShallow(Visitor visitor) throws ModelDBException {
        return visitor.postVisitVersionEnvironmentBlob(this);
    }

    public VersionEnvironmentBlob postVisitDeep(Visitor visitor) throws ModelDBException {
        this.Major = visitor.postVisitDeepInteger(this.Major);
        this.Minor = visitor.postVisitDeepInteger(this.Minor);
        this.Patch = visitor.postVisitDeepInteger(this.Patch);
        this.Suffix = visitor.postVisitDeepString(this.Suffix);
        return this.postVisitShallow(visitor);
    }
}
