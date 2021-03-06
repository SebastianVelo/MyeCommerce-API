package com.myecommerce.enumerator;

public enum UserMessages implements Message {

	INSERT_OK ("¡Usuario creado con exito!"),
	INSERT_ERR_USERNAME ("El usuario ya ha sido utilizado."),
	INSERT_ERR_EMAIL ("El email ya ha sido utilizado."),
	INSERT_ERR ("Ha ocurrido un error al crear el usuario."),
	
	DELETE_OK ("¡Usuario borrado con exito!"),
	DELETE_ERR ("Ha ocurrido un error al borrar el usuario."),
	
	GET_OK ("¡Usuario encontrado!"),
	GET_ERR ("Ha ocurrido un error al intentar encontrar el usuario."),

	CHECK_OK ("¡Credenciales correctas!"),
	CHECK_ERR ("El usuario o la contraseña son invalidos."),
	
	ERR_NOT_EXISTS ("El usuario seleccionado no existe.");
	
	private final String msg;

	private UserMessages(String msg) {
        this.msg = msg;
    }
    
	@Override
    public String getMsg() {
        return this.msg;
    }
}
