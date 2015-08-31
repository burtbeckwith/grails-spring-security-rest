/*
 * Copyright 2013-2015 Alvaro Sanchez-Mariscal <alvaro.sanchezmariscal@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package grails.plugin.springsecurity.rest.credentials

import org.grails.plugins.testing.GrailsMockHttpServletRequest

import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * Specification of all the credentials extractors
 */
@TestMixin(GrailsUnitTestMixin)
class CredentialsExtractorTestSpec extends Specification {

    def setup(){
        ReflectionUtils.application = grailsApplication
    }

    void "credentials can be extracted from a JSON request"() {
        given:
        def request = new GrailsMockHttpServletRequest()
        request.json = '{"username": "foo", "password": "bar"}'
        def extractor = new DefaultJsonPayloadCredentialsExtractor(usernamePropertyName: 'username', passwordPropertyName: 'password')

        and: "Spring security configuration"
        SpringSecurityUtils.loadSecondaryConfig 'DefaultRestSecurityConfig'

        when:
        def token = extractor.extractCredentials(request)

        then:
        token.principal == 'foo'
        token.credentials == 'bar'

    }

    void "JSON parsing handles unexpected requests"() {

        given:
        def request = new GrailsMockHttpServletRequest()
        request.json = '{"different": "format", "of": "JSON"}'
        def extractor = new DefaultJsonPayloadCredentialsExtractor(usernamePropertyName: 'username', passwordPropertyName: 'password')

        when:
        def token = extractor.extractCredentials(request)

        then:
        !token.principal
        !token.credentials


    }

    void "credentials can be extracted from a request params-based request"() {

        given:
        def request = new GrailsMockHttpServletRequest()
        request.parameters = [username: 'foo', password: 'bar']
        def extractor = new RequestParamsCredentialsExtractor(usernamePropertyName: 'username', passwordPropertyName: 'password')

        when:
        def token = extractor.extractCredentials(request)

        then:
        token.principal == 'foo'
        token.credentials == 'bar'

    }

    void "credentials can be extracted from a JSON request with custom configuration"(){

        given:
        def request = new GrailsMockHttpServletRequest()
        request.json = '{"login": "foo", "pwd": "bar"}'
        def extractor = new DefaultJsonPayloadCredentialsExtractor(usernamePropertyName: 'login', passwordPropertyName: 'pwd')

        when:
        def token = extractor.extractCredentials(request)

        then:
        token.principal == 'foo'
        token.credentials == 'bar'

    }

}
