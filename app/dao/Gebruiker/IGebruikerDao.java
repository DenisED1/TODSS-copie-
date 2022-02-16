package dao.Gebruiker;

import model.Gebruiker;

import java.util.List;

public interface IGebruikerDao {
    void insert(Gebruiker u);

    Gebruiker getByID(int id);

    Gebruiker getByUUID(String uuId);

    Gebruiker getByUsername(String uuId);

    List<Gebruiker> getAlleUsersVanBegeleider(Gebruiker gebruiker);

    List<Gebruiker> getAlleUsersVanFunctie(Gebruiker.Functie functie);

    List<Gebruiker> getAlleUsersVanFuncties(List<Gebruiker.Functie> functies);

    void update(Gebruiker gebruiker);

    void remove(Gebruiker gebruiker);

    void remove(String uuId);
}
