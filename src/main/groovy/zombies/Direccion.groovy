package zombies;

class Direccion {
  static require() {
    Number.metaClass {
      derecha = { delegate }
      izquierda = { -delegate }
    }
  }
}
