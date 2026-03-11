package src.services;

import src.dao.InvigilatorDAO;
import src.models.Invigilator;
import src.validators.InvigilatorValidator;

import java.util.List;

public class InvigilatorService {

    private final InvigilatorDAO dao = new InvigilatorDAO();

    // -------------------------
    // CREATE
    // -------------------------
    public Invigilator add(Invigilator invigilator) {

        if (invigilator == null) {
            throw new IllegalArgumentException(
                    "Invigilator information cannot be empty."
            );
        }

        InvigilatorValidator.validate(invigilator);

        try {

            // check duplicate email
            Invigilator existing = dao.getByEmail(invigilator.getEmail());

            if (existing != null) {
                throw new IllegalArgumentException(
                        "An invigilator with this email already exists."
                );
            }

            return dao.insert(invigilator);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create the invigilator."
            );
        }
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<Invigilator> getAll() {

        try {

            return dao.getAll();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve invigilator records."
            );
        }
    }

    // -------------------------
    // READ BY ID
    // -------------------------
    public Invigilator getById(int invigilatorId) {

        try {

            Invigilator invigilator = dao.getById(invigilatorId);

            if (invigilator == null) {
                throw new IllegalArgumentException(
                        "The selected invigilator could not be found."
                );
            }

            return invigilator;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve the invigilator."
            );
        }
    }

    // -------------------------
    // READ BY DEPARTMENT
    // -------------------------
    public List<Invigilator> getByDepartment(String department) {

        try {

            return dao.getByDepartment(department);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve invigilators for this department."
            );
        }
    }

    // -------------------------
    // READ BY EMAIL
    // -------------------------
    public Invigilator getByEmail(String email) {

        try {

            Invigilator invigilator = dao.getByEmail(email);

            if (invigilator == null) {
                throw new IllegalArgumentException(
                        "No invigilator found with the provided email."
                );
            }

            return invigilator;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve the invigilator."
            );
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public boolean deleteById(int invigilatorId) {

        try {

            Invigilator invigilator = dao.getById(invigilatorId);

            if (invigilator == null) {
                throw new IllegalArgumentException(
                        "The selected invigilator could not be found."
                );
            }

            return dao.delete(invigilatorId);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to delete the invigilator."
            );
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Invigilator update(Invigilator invigilator) {

        if (invigilator == null) {
            throw new IllegalArgumentException(
                    "Invigilator information cannot be empty."
            );
        }

        InvigilatorValidator.validate(invigilator);

        try {

            Invigilator existing = dao.getById(invigilator.getInvigilatorId());

            if (existing == null) {
                throw new IllegalArgumentException(
                        "The selected invigilator does not exist."
                );
            }

            return dao.update(invigilator);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to update the invigilator."
            );
        }
    }
}