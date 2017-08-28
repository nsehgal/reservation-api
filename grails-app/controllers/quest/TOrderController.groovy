package quest

import grails.util.TypeConvertingMap

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TOrderController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TOrder.list(params), model:[TOrderCount: TOrder.count()]
    }

    def show(TOrder TOrder) {
        respond TOrder
    }

    def create() {
        respond new TOrder(params)
    }

    @Transactional
    def save() {

        def params = (request.JSON as TypeConvertingMap) + (params as TypeConvertingMap)
        if(!params.user.email) {
            respond params, [status: BAD_REQUEST]
            return
        }
        TeslaUser user = TeslaUser.findByEmail(params.user.email)
        if(!user) {respond params, [status: BAD_REQUEST];return}

        TOrder order = new TOrder()
        order.confirmed = true
        order.user = user
        order.save()
        params.products.each{
            TProduct product = TProduct.findByKey(it.key)
            TorderTProduct torderTProduct = new TorderTProduct(tProduct: product, tOrder: order, specs: it.specs)
            torderTProduct.save()
        }
        respond order, [status: OK]
    }


    def edit(TOrder TOrder) {
        respond TOrder
    }

    @Transactional
    def update(TOrder TOrder) {
        if (TOrder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (TOrder.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond TOrder.errors, view:'edit'
            return
        }

        TOrder.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TOrder.label', default: 'TOrder'), TOrder.id])
                redirect TOrder
            }
            '*'{ respond TOrder, [status: OK] }
        }
    }

    @Transactional
    def delete(TOrder TOrder) {

        if (TOrder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        TOrder.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TOrder.label', default: 'TOrder'), TOrder.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'TOrder.label', default: 'TOrder'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
