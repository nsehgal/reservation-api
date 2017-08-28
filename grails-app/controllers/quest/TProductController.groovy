package quest

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TProductController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TProduct.list(params), model:[TProductCount: TProduct.count()]
    }

    def show(TProduct TProduct) {
        respond TProduct
    }

    def create() {
        respond new TProduct(params)
    }

    @Transactional
    def save(TProduct TProduct) {
        if (TProduct == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (TProduct.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond TProduct.errors, view:'create'
            return
        }

        TProduct.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'TProduct.label', default: 'TProduct'), TProduct.id])
                redirect TProduct
            }
            '*' { respond TProduct, [status: CREATED] }
        }
    }

    def edit(TProduct TProduct) {
        respond TProduct
    }

    @Transactional
    def update(TProduct TProduct) {
        if (TProduct == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (TProduct.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond TProduct.errors, view:'edit'
            return
        }

        TProduct.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TProduct.label', default: 'TProduct'), TProduct.id])
                redirect TProduct
            }
            '*'{ respond TProduct, [status: OK] }
        }
    }

    @Transactional
    def delete(TProduct TProduct) {

        if (TProduct == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        TProduct.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TProduct.label', default: 'TProduct'), TProduct.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'TProduct.label', default: 'TProduct'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
