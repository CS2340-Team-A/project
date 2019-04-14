package com.cs2340.teama.viewModels;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.cs2340.teama.models.Coordinates;
import com.cs2340.teama.models.Game;
import com.cs2340.teama.models.Planet;
import com.cs2340.teama.models.Ship;
import com.cs2340.teama.models.SolarSystem;
import com.cs2340.teama.models.realm.PlayerModel;
import com.cs2340.teama.models.realm.ShipModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * ViewModel containing business logic for the TravelActivity
 */
public class TravelViewModel extends ViewModel {

    private final Game game = Game.getInstance();

    private Planet getPlanet() {
        return game.getPlanet();
    }

    /**
     * @return the planets information
     */
    public CharSequence getPlanetInfo() {
        return this.getPlanet().getInfo();
    }

    /**
     * @return the ship
     */
    public Ship getShip() {
        return game.getPlayer().getShip();
    }

    /**
     * Gets a list of reachable planets
     * @return List of Reachable Planets*/
    public List<Planet> getPlanetList(){
        List<Planet> reachablePlanets = new ArrayList<>();
        Coordinates playerCoords = game.getPlayer().getCoordinates();
        double distToPlanet;
        Ship ship = game.getPlayer().getShip();
        for (SolarSystem system : game.getUniverse().getSolarSystems()) {

            distToPlanet = Coordinates.distTo(playerCoords, system.getCoordinates());
            if (ship.canTravelDist(distToPlanet)) {
                reachablePlanets.add(system.getPlanet());
            }

        }
        return reachablePlanets;
    }

    /**
     * Gets a list of reachable planets
     * @return List of Reachable Planets
     */
    public List<String> getPlanetNameList() {
        List<String> reachablePlanets = new ArrayList<>();
        Coordinates playerCoords = game.getPlayer().getCoordinates();
        double distToPlanet;
        Ship ship = game.getPlayer().getShip();
        for (SolarSystem system : game.getUniverse().getSolarSystems()) {

            distToPlanet = Coordinates.distTo(playerCoords, system.getCoordinates());
            if (ship.canTravelDist(distToPlanet)) {
                reachablePlanets.add(system.getPlanet().getName());
            }

        }
        return reachablePlanets;
    }

    /**
     * @return list containing all solar systems
     */
    private List<SolarSystem> getSolarSystems() {
        List<SolarSystem> reachableSystems = new ArrayList<>();
        Coordinates playerCoords = game.getPlayer().getCoordinates();
        double distToPlanet;
        Ship ship = game.getPlayer().getShip();
        for (SolarSystem system : game.getUniverse().getSolarSystems()) {

            distToPlanet = Coordinates.distTo(playerCoords, system.getCoordinates());
            if (ship.canTravelDist(distToPlanet)) {
                reachableSystems.add(system);
            }

        }
        return reachableSystems;
    }

    /**
     * @param planetPos destination planet
     */
    public void travelTo(int planetPos) {
        Planet planet = this.getPlanetList().get(planetPos);
        Log.d("Edit", "Got planet " + planet);

        SolarSystem system = this.getSolarSystems().get(planetPos);
        final Coordinates destCoord = system.getCoordinates();

        game.getPlayer().travel(destCoord);

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PlayerModel playerModel = realm.where(PlayerModel.class).findFirst();
                if (playerModel != null) {
                    ShipModel shipModel = playerModel.getShip();
                    shipModel.removeFuel(Coordinates.distTo(
                            playerModel.getCoordinates(),
                            destCoord
                    ) / Ship.FUEL_EFFICIENCY);
                    playerModel.setCoordinates(destCoord.getX(), destCoord.getY());
                }
            }
        });
        realm.close();
    }
}
