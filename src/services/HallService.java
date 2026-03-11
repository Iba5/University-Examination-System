package src.services;

import src.dao.HallDAO;
import src.models.Hall;
import src.validators.HallValidator;

import java.util.ArrayList;
import java.util.List;

public class HallService {

    private final HallDAO dao = new HallDAO();

    // -------------------------
    // CREATE (batch)
    // -------------------------
    public List<Hall> add(List<Hall> halls) {

        if(halls == null || halls.isEmpty()){
            throw new IllegalArgumentException(
                    "No halls were provided."
            );
        }

        List<Hall> newHalls = new ArrayList<>();

        try {

            for (Hall hall : halls) {

                if (hall == null) {
                    throw new IllegalArgumentException(
                            "Hall information cannot be empty."
                    );
                }

                HallValidator.validate(hall);

                Hall existing = dao.getById(hall.getHallId());

                if(existing != null){
                    throw new IllegalArgumentException(
                            "A hall with this ID already exists."
                    );
                }

                Hall added = dao.insert(hall);

                newHalls.add(added);
            }

            return newHalls;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create halls. Please try again."
            );
        }
    }

    // -------------------------
    // CREATE (single)
    // -------------------------
    public Hall addSingle(Hall hall) {

        if(hall == null){
            throw new IllegalArgumentException(
                    "Hall information cannot be empty."
            );
        }

        HallValidator.validate(hall);

        try {

            Hall existing = dao.getById(hall.getHallId());

            if(existing != null){
                throw new IllegalArgumentException(
                        "A hall with this ID already exists."
                );
            }

            return dao.insert(hall);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create the hall."
            );
        }
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<Hall> getAll() {

        try {

            return dao.getAll();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve hall records."
            );
        }
    }

    // -------------------------
    // READ BY ID
    // -------------------------
    public Hall getById(int hallId) {

        try {

            Hall hall = dao.getById(hallId);

            if(hall == null){
                throw new IllegalArgumentException(
                        "The selected hall could not be found."
                );
            }

            return hall;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve the hall information."
            );
        }
    }

    // -------------------------
    // READ BY NAME
    // -------------------------
    public Hall getByName(String hallName) {

        try {

            Hall hall = dao.getByName(hallName);

            if(hall == null){
                throw new IllegalArgumentException(
                        "The selected hall could not be found."
                );
            }

            return hall;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve the hall."
            );
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Hall update(Hall hall) {

        if(hall == null){
            throw new IllegalArgumentException(
                    "Hall information cannot be empty."
            );
        }

        HallValidator.validate(hall);

        try {

            Hall existing = dao.getById(hall.getHallId());

            if(existing == null){
                throw new IllegalArgumentException(
                        "The selected hall does not exist."
                );
            }

            return dao.update(hall);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to update the hall."
            );
        }
    }

    // -------------------------
    // DELETE BY NAME
    // -------------------------
    public Hall delete(String name) {

        try {

            Hall hall = dao.getByName(name);

            if (hall == null) {
                throw new IllegalArgumentException(
                        "The selected hall could not be found."
                );
            }

            dao.delete(hall.getHallId());

            return hall;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to delete the hall."
            );
        }
    }

    // -------------------------
    // DELETE BY ID
    // -------------------------
    public boolean deleteById(int hallId) {

        try {

            Hall hall = dao.getById(hallId);

            if(hall == null){
                throw new IllegalArgumentException(
                        "The selected hall could not be found."
                );
            }

            return dao.delete(hallId);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to delete the hall."
            );
        }
    }
}