#!/bin/bash

set -e

BASE="../protos/gen/swagger/protos/public/modeldb/versioning/VersioningService.swagger.json"
TARGET="src/main/java/ai/verta/modeldb/versioning/autogenerated"

rm -rf $TARGET

for f in $(find $BASE -type f | sort)
do
    echo "Processing $f"
    ../client/tools/swagger_codegen.py --input $f --output-dir $TARGET --templates templates --file-suffix java --case capital

    echo ""
done

for f in $(find $TARGET -type f | grep -v 'Blob.java')
do
    rm $f
done

for f in $(find $TARGET -type f)
do
    (cat $f | sed 's,Versioning,,g') > $(dirname $f)/$(basename $f | sed 's,^Versioning,,')
    rm $f
done

VISITOR="src/main/java/ai/verta/modeldb/versioning/blob/visitors/Visitor.java"

cat > $VISITOR <<EOF
package ai.verta.modeldb.versioning.blob.visitors;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Visitor {
EOF
for f in $(find $TARGET -type f | grep 'Blob.java$' | sort)
do
    type=$(basename $f | sed 's,\.java$,,')
    cat >> $VISITOR <<EOF
    public void preVisitListOf${type}(List<${type}> lst) throws ModelDBException {
        for (${type} val : lst) {
            preVisit${type}(val);
        }
    }

    public void preVisitDeepListOf${type}(List<${type}> lst) throws ModelDBException {
        for (${type} val : lst) {
            preVisitDeep${type}(val);
        }
    }

    public List<${type}> postVisitListOf${type}(List<${type}> lst) throws ModelDBException {
        final List<${type}> collect = new ArrayList<>(lst.size());
        for (${type} val : lst) {
            collect.add(postVisit${type}(val));
        }
        return collect;
    }

    public List<${type}> postVisitDeepListOf${type}(List<${type}> lst) throws ModelDBException {
        final List<${type}> collect = new ArrayList<>(lst.size());
        for (${type} val : lst) {
            collect.add(postVisitDeep${type}(val));
        }
        return collect;
    }

    public void preVisit${type}(${type} blob) throws ModelDBException {}
    public void preVisitDeep${type}(${type} blob) throws ModelDBException {}
    public ${type} postVisit${type}(${type} blob) throws ModelDBException { return blob; }
    public ${type} postVisitDeep${type}(${type} blob) throws ModelDBException { return blob.postVisitDeep(this); }
EOF
done
for type in String Boolean Double Integer
do
    cat >> $VISITOR <<EOF
    public void preVisitListOf${type}(List<${type}> lst) throws ModelDBException {
        for (${type} val : lst) {
            preVisit${type}(val);
        }
    }

    public void preVisitDeepListOf${type}(List<${type}> lst) throws ModelDBException {
        for (${type} val : lst) {
            preVisitDeep${type}(val);
        }
    }

    public List<${type}> postVisitListOf${type}(List<${type}> lst) throws ModelDBException {
        final List<${type}> collect = new ArrayList<>(lst.size());
        for (${type} val : lst) {
            collect.add(postVisit${type}(val));
        }
        return collect;
    }

    public List<${type}> postVisitDeepListOf${type}(List<${type}> lst) throws ModelDBException {
        final List<${type}> collect = new ArrayList<>(lst.size());
        for (${type} val : lst) {
            collect.add(postVisitDeep${type}(val));
        }
        return collect;
    }

    public void preVisit${type}(${type} blob) throws ModelDBException {}
    public void preVisitDeep${type}(${type} blob) throws ModelDBException {}
    public ${type} postVisit${type}(${type} blob) throws ModelDBException { return blob; }
    public ${type} postVisitDeep${type}(${type} blob) throws ModelDBException { return blob; }
EOF
done
cat >> $VISITOR <<EOF
}
EOF
