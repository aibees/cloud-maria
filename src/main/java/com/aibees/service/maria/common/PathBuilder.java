package com.aibees.service.maria.common;

public class PathBuilder {
    private final String path;

    private PathBuilder() { this.path = ""; }

    private PathBuilder(String path) { this.path = path; }

    public String getPath() { return this.path; }

    public static class start {
        private String path;

        public start setBasePath(String base) {
            this.path = base;
            return this;
        }

        public start addPath(String path) {
            this.path = this.path.concat("\\").concat(path);
            return this;
        }

        public String end() {
            return new PathBuilder(this.path).getPath();
        }
    }
}
