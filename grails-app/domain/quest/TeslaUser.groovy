package quest

class TeslaUser {

    String email
    String username


    static constraints = {
        email nullable: false, unique: true
        username nullable: false, unique: true
    }
}
