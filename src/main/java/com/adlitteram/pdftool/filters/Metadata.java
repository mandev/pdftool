package com.adlitteram.pdftool.filters;

public class Metadata {

    protected String key;
    protected String value;

    /**
     *
     * @param key
     * @param value
     */
    public Metadata(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Metadata other = (Metadata) obj;
        if ((this.getKey() == null) ? (other.getKey() != null) : !this.key.equals(other.key)) {
            return false;
        }
        if ((this.getValue() == null) ? (other.getValue() != null) : !this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (this.getKey() != null ? this.getKey().hashCode() : 0);
        hash = 61 * hash + (this.getValue() != null ? this.getValue().hashCode() : 0);
        return hash;
    }
}
