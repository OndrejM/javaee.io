package io.javaee

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoProjectInfo {
    String configFile
    String name
    String friendlyName
    String description
    String home
    Collection<String> related
    Collection<DtoProjectResource> resources
    boolean spec

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }
        DtoProjectInfo that = (DtoProjectInfo) o
        if (configFile != that.configFile) {
            return false
        }
        return true
    }

    int hashCode() {
        return configFile.hashCode()
    }
}
