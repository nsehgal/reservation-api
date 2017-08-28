package quest

class TOrder {

    UUID uuid = UUID.randomUUID()
    Boolean confirmed
    Date orderDate
    TeslaUser user
    String specs

    static hasMany = [products: TorderTProduct]


    static constraints = {
        uuid unique: true
        confirmed nullable: true
        orderDate nullable: true
        user nullable: true
        specs nullable: true
        products nullable: true
    }

    static mapping = {
        products joinTable: [name: "tordertproduct", key: 't_order_id' ]
    }
}
