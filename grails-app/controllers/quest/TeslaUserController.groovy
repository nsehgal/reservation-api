package quest

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TeslaUserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TeslaUser.list(params), model:[teslaUserCount: TeslaUser.count()]
    }

    def show(TeslaUser teslaUser) {
        respond teslaUser
    }

    def create() {
        respond new TeslaUser(params)
    }

    @Transactional
    def save(TeslaUser teslaUser) {
        if (teslaUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (teslaUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond teslaUser.errors, view:'create'
            return
        }

        teslaUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'teslaUser.label', default: 'TeslaUser'), teslaUser.id])
                redirect teslaUser
            }
            '*' { respond teslaUser, [status: CREATED] }
        }
    }

    def edit(TeslaUser teslaUser) {
        respond teslaUser
    }

    @Transactional
    def update(TeslaUser teslaUser) {
        if (teslaUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (teslaUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond teslaUser.errors, view:'edit'
            return
        }

        teslaUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'teslaUser.label', default: 'TeslaUser'), teslaUser.id])
                redirect teslaUser
            }
            '*'{ respond teslaUser, [status: OK] }
        }
    }

    @Transactional
    def delete(TeslaUser teslaUser) {

        if (teslaUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        teslaUser.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'teslaUser.label', default: 'TeslaUser'), teslaUser.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'teslaUser.label', default: 'TeslaUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
