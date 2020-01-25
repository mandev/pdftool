package com.adlitteram.pdftool;

public class Dimension {

    public static class Float {

        public float width;
        public float height;

        /**
         *
         * @param w
         * @param h
         */
        public Float(float w, float h) {
            this.width = w;
            this.height = h;
        }

        /**
         *
         * @return
         */
        public float getWidth() {
            return width;
        }

        /**
         *
         * @return
         */
        public float getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "Dimension.Float[" + width + ", " + height + "]";
        }
    }

    /**
     *
     */
    public static class Double {

        /**
         *
         */
        public double width;
        /**
         *
         */
        public double height;

        /**
         *
         * @param w
         * @param h
         */
        public Double(double w, double h) {
            this.width = w;
            this.height = h;
        }

        /**
         *
         * @return
         */
        public double getWidth() {
            return width;
        }

        /**
         *
         * @return
         */
        public double getHeight() {
            return height;
        }

        @Override
        public String toString() {
            return "Dimension.Double[" + width + ", " + height + "]";
        }
    }
}
