package org.ikora.utils;

import org.apache.commons.lang3.tuple.Pair;

public class EasyPair<L,R> extends Pair<L, R> {
        public final L left;
        public final R right;
        public Integer hashCode;

    public static <L, R> EasyPair<L, R> of(final L left, final R right) {
        return new EasyPair<>(left, right);
    }

        private EasyPair(){
            throw new NullPointerException();
        }

        public EasyPair(final L left, final R right) {
            super();
            this.left = left;
            this.right = right;
            this.hashCode = null;
        }

        @Override
        public L getLeft() {
            return left;
        }

        @Override
        public R getRight() {
            return right;
        }

        @Override
        public R setValue(final R value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode(){
            if(this.hashCode == null){
                hashCode = super.hashCode();
            }

            return hashCode;
        }

}
