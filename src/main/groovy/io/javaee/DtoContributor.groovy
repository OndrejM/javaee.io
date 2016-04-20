package io.javaee

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true, includeFields = true, excludes = ['metaClass'])
class DtoContributor {
    String login
    Collection<String> projects = new HashSet<>()
    int contributions = 0
}
