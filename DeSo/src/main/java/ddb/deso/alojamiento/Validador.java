package ddb.deso.alojamiento;

import java.time.LocalDate;

import ddb.deso.TipoDoc;


public class Validador {

    public static boolean validadorLetras(String texto) {
    return texto.trim().matches("[\\p{L} ]+");
}
    
    public static boolean validadorNumero(String numero) {
    return numero.matches("\\d+");
}
    
    public static boolean validadorDni(String dni) {
    return dni.matches("\\d{7,8}");
}
    
    public static boolean validadorLE(String le) {
    return le.matches("\\d{8}");
}
    
    public static boolean validadorLC(String lc) {
    return lc.matches("\\d{1,7}");
}
    
    public static boolean validadorPasaporte(String pasaporte) {
    return pasaporte.matches("[A-Z]{3}\\d{6}");
}

    public static boolean validadorCuit(String cuit) {
    if (cuit == null) return false;
 
    cuit = cuit.replaceAll("-", "");
 
    if (!cuit.matches("\\d{11}")) return false;

    // Cálculo del dígito verificador según AFIP
    int[] pesos = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
    int suma = 0;

    for (int i = 0; i < 10; i++) {
        int num = Character.getNumericValue(cuit.charAt(i));
        suma += num * pesos[i];
    }

    int resto = suma % 11;
    int verificador = resto == 0 ? 0 : (resto == 1 ? 9 : 11 - resto);

    int ultimoDigito = Character.getNumericValue(cuit.charAt(10));

    return verificador == ultimoDigito;
}

    public static boolean gmailValido(String email) {
    if (email == null) return false;
    return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    public static boolean isApellidoValido(String apellido){
        return apellido != null && !apellido.isBlank() && validadorLetras(apellido);
    }

    public static boolean isNombreValido(String nombres){
        return nombres != null && !nombres.isBlank() && validadorLetras(nombres);
    }

    public static boolean isTipoDocumentoValido(TipoDoc tipoDocumento){
        return tipoDocumento != null;
    }

    public static boolean isNumeroDocumentoValido(String numeroDocumento, TipoDoc tipoDoc){
        if(numeroDocumento != null && !numeroDocumento.isBlank()){
            switch (tipoDoc) {
                case DNI ->  { return validadorDni(numeroDocumento); }
                case LC ->  { return validadorDni(numeroDocumento); }
                case LE ->  { return validadorLE(numeroDocumento); }
                case PASAPORTE ->  { return validadorPasaporte(numeroDocumento); }
                case OTRO ->  { return true; }
                default -> { return false; }
            }
        }
        return false;
    }

    public static boolean isPosicionIvaValida(String posicionIVA){
        return posicionIVA != null && !posicionIVA.isBlank() && validadorLetras(posicionIVA);
    }

    public static boolean isFechaNacimientoValida(LocalDate fechaNacimiento){
        return fechaNacimiento != null && fechaNacimiento.isBefore(LocalDate.now());
    }

    public static boolean isCalleValida(String calle){
        return calle != null && !calle.isBlank() && validadorLetras(calle);
    }

    public static boolean isNumeroCalleValido(String numeroCalle){
        return numeroCalle != null && !numeroCalle.isBlank();
    }

    public static boolean isCodigoPostalValido(String codigoPostal){
        return codigoPostal != null && !codigoPostal.isBlank();
    }

    public static boolean isLocalidadValida(String localidad){
        return localidad != null && !localidad.isBlank() && validadorLetras(localidad);
    }

    public static boolean isProvinciaValida(String provincia){
        return provincia != null && !provincia.isBlank() && validadorLetras(provincia);
    }

    public static boolean isPaisValido(String pais){
        return pais != null && !pais.isBlank() && validadorLetras(pais);
    }

    public static boolean isTelefonoValido(String telefono){
        return telefono != null && !telefono.isBlank() && validadorNumero(telefono);
    }

    public static boolean isOcupacionValida(String ocupacion){
        return ocupacion != null && !ocupacion.isBlank() && validadorLetras(ocupacion);
    }

    public static boolean isNacionalidadValida(String nacionalidad){
        return nacionalidad != null && !nacionalidad.isBlank() && validadorLetras(nacionalidad);
    }

    // opcionales: cuit y email aceptan vacío; si se proporcionan se validan
    public static boolean isCuitValidoOpcional(String cuit){
        if (cuit == null || cuit.isBlank()) return true;
        return validadorCuit(cuit);
    }

    public static boolean isEmailValidoOpcional(String email){
        if (email == null || email.isBlank()) return true;
        return gmailValido(email);
    }

}