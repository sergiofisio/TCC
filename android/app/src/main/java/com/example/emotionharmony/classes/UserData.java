package com.example.emotionharmony.classes;

public class UserData {
    private static String peso;
    private static String horaInicio;
    private static String horaFim;

    public static String getPeso() {
        return peso;
    }

    public static void setPeso(String peso) {
        UserData.peso = peso;
    }

    public static String getHoraInicio() {
        return horaInicio;
    }

    public static void setHoraInicio(String horaInicio) {
        UserData.horaInicio = horaInicio;
    }

    public static String getHoraFim() {
        return horaFim;
    }

    public static void setHoraFim(String horaFim) {
        UserData.horaFim = horaFim;
    }
}
