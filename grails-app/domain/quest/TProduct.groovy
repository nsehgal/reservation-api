package quest

class TProduct {

    String name
    String key
    String specs

    static hasMany = [orders: TorderTProduct]


    static constraints = {
        key unique: true
    }

    static mapping = {
        products joinTable: [name: "tordertproduct", key: 't_product_id' ]
    }
}
