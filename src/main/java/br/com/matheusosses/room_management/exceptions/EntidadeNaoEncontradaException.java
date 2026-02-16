package br.com.matheusosses.room_management.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException{
    public EntidadeNaoEncontradaException(String msg){
        super(msg);
    }
}
