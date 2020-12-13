package api;

/**
 * This class represents a geo location <x,y,z>, aka Point3D
 */
public class GeoLocation implements geo_location {
        private double x;
        private double y;
        private double z;

        public GeoLocation() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public GeoLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public GeoLocation(geo_location g) {
            this.x = g.x();
            this.y = g.y();
            this.z = g.z();
        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double z() {
            return z;
        }

        @Override
        public double distance(geo_location g) {
            return Math.sqrt(Math.pow((this.x-g.x()),2)+Math.pow((this.y-g.y()),2)+Math.pow((this.z-g.z()),2));
        }
    }

