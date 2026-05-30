package com.magazin;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DB_FOLDER = "data";

    private static void asiguraDirector() {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void salveazaProduse(List<Produs> produse) throws IOException {
        asiguraDirector();
        mapper.writeValue(new File(DB_FOLDER, "produse.json"), produse);
    }

    public static List<Produs> incarcaProduse() throws IOException {
        File file = new File(DB_FOLDER, "produse.json");
        if (!file.exists())
            return null;
        return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Produs.class));
    }

    public static void salveazaUtilizatori(List<Utilizator> utilizatori) throws IOException {
        asiguraDirector();
        mapper.writeValue(new File(DB_FOLDER, "utilizatori.json"), utilizatori);
    }

    public static List<Utilizator> incarcaUtilizatori() throws IOException {
        File file = new File(DB_FOLDER, "utilizatori.json");
        if (!file.exists())
            return null;
        return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Utilizator.class));
    }

    public static void salveazaOferte(List<Oferta> oferte) throws IOException {
        asiguraDirector();
        mapper.writeValue(new File(DB_FOLDER, "oferte.json"), oferte);
    }

    public static List<Oferta> incarcaOferte() throws IOException {
        File file = new File(DB_FOLDER, "oferte.json");
        if (!file.exists())
            return null;
        return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Oferta.class));
    }

    public static void salveazaIstoric(List<String> istoric) throws IOException {
        asiguraDirector();
        mapper.writeValue(new File(DB_FOLDER, "istoric.json"), istoric);
    }

    public static List<String> incarcaIstoric() throws IOException {
        File file = new File(DB_FOLDER, "istoric.json");
        if (!file.exists())
            return null;
        return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
    }
}