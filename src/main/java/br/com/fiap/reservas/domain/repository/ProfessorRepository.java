package br.com.fiap.reservas.domain.repository;
import br.com.fiap.reservas.domain.model.Professor;
import java.util.*;
public interface ProfessorRepository { Professor save(Professor p); Optional<Professor> findById(Long id); List<Professor> findAll(); }
