package io.javaee

import javax.annotation.PostConstruct
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Startup

@Singleton
@Startup
@Lock(LockType.READ)
class ServiceApplication {
    private URL documents
    private String githubAuthToken

    @PostConstruct
    void postConstruct() {
        this.githubAuthToken = System.getProperty("io.github.token", System.getenv()['github_atoken'])
        if (!this.githubAuthToken) {
            throw new ExceptionApplication("no github authentication token defined")
        }
    }

    @Lock(LockType.WRITE)
    void init(URL documents) {
        this.documents = documents
    }

    URL getDocuments() {
        return documents
    }

    String getGithubAuthToken() {
        return githubAuthToken
    }
}
