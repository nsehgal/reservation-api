package org.tesla

import grails.test.mixin.*
import quest.AppUser
import quest.AppUserController
import spock.lang.*

@TestFor(AppUserController)
@Mock(AppUser)
class AppUserControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.appUserList
            model.appUserCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.appUser!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def appUser = new AppUser()
            appUser.validate()
            controller.save(appUser)

        then:"The create view is rendered again with the correct model"
            model.appUser!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            appUser = new AppUser(params)

            controller.save(appUser)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/appUser/show/1'
            controller.flash.message != null
            AppUser.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def appUser = new AppUser(params)
            controller.show(appUser)

        then:"A model is populated containing the domain instance"
            model.appUser == appUser
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def appUser = new AppUser(params)
            controller.edit(appUser)

        then:"A model is populated containing the domain instance"
            model.appUser == appUser
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/appUser/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def appUser = new AppUser()
            appUser.validate()
            controller.update(appUser)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.appUser == appUser

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            appUser = new AppUser(params).save(flush: true)
            controller.update(appUser)

        then:"A redirect is issued to the show action"
            appUser != null
            response.redirectedUrl == "/appUser/show/$appUser.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/appUser/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def appUser = new AppUser(params).save(flush: true)

        then:"It exists"
            AppUser.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(appUser)

        then:"The instance is deleted"
            AppUser.count() == 0
            response.redirectedUrl == '/appUser/index'
            flash.message != null
    }
}