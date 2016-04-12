package io.javaee

import org.tomitribe.sabot.Config

import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.inject.Inject

@Singleton
@Lock(LockType.READ)
class ServiceApplication {
    @Inject
    @Config(value = 'github_atoken')
    private String githubAuthToken

    String getGithubAuthToken() {
        return githubAuthToken
    }
}

