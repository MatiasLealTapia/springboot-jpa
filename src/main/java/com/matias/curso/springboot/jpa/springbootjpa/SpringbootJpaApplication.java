package com.matias.curso.springboot.jpa.springbootjpa;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.matias.curso.springboot.jpa.springbootjpa.entities.Person;
import com.matias.curso.springboot.jpa.springbootjpa.repositories.PersonRepository;

@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner {

	@Autowired
	private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		personalizedQueries();
	}

	@Transactional(readOnly = true)
	public void personalizedQueries() {

		Scanner scanner = new Scanner(System.in);

		System.out.println("=========== Consulta solo el nombre por el Id ===========");
		System.out.println("Ingrese el ID para leer nombre de persona:");
		Long id = scanner.nextLong();
		scanner.close();

		System.out.println("===== Mostrando solo");
		String name = repository.getNameById(id);
		System.out.println(name);

		Long idDb = repository.getIdById(id);
		System.out.println(idDb);

		String fullName = repository.getFullNameById(id);
		System.out.println(fullName);

		System.out.println("Consulta por campos personalizados por el ID: ");
		Object[] personReg = repository.obtenerPersonDataById(idDb);
	}

	@Transactional
	public void delete2() {
		repository.findAll().forEach(System.out::println);

		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese ID de la persona a eliminar:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);
		optionalPerson.ifPresent(repository::delete);
		optionalPerson.ifPresentOrElse(
				person -> repository.delete(person),
				() -> System.out.println("Lo sentimos, no existe la persona con ese ID!"));
		repository.deleteById(id);

		repository.findAll().forEach(System.out::println);
		scanner.close();
	}

	@Transactional
	public void delete() {
		repository.findAll().forEach(System.out::println);

		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese ID de la persona a eliminar:");
		Long id = scanner.nextLong();
		repository.deleteById(id);

		repository.findAll().forEach(System.out::println);
		scanner.close();
	}

	@Transactional
	public void update() {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese ID de la persona a editar:");
		Long id = scanner.nextLong();
		Optional<Person> optionalPerson = repository.findById(id);

		// optionalPerson.ifPresent(person -> {
		if (optionalPerson.isPresent()) {
			Person personDB = optionalPerson.orElseThrow();

			System.out.println(personDB);
			System.out.println("Ingrese el lenguaje de programación:");
			String programmingLanguage = scanner.next();
			personDB.setProgrammingLanguage(programmingLanguage);
			Person personUpdated = repository.save(personDB);
			System.out.println(personUpdated);
		} else {
			System.out.println("El usuario no existe!");
		}
		// });

		scanner.close();
	}

	@Transactional
	public void create() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el nombre: ");
		String name = scanner.next();
		System.out.println("Ingrese el apellido: ");
		String lastname = scanner.next();
		System.out.println("Ingrese el lenguaje de programación: ");
		String programmingLanguage = scanner.next();
		scanner.close();
		Person person = new Person(null, name, lastname, programmingLanguage);
		// Person person = new Person(null, "Lalo", "Thor", "Phyton");
		Person personNew = repository.save(person);
		System.out.println(personNew);

		repository.findById(personNew.getId()).ifPresent(System.out::println);

	}

	@Transactional(readOnly = true)
	public void findOne() {
		// Person person = null;
		// Optional<Person> optionalPerson = repository.findById(1L);
		// if (optionalPerson.isPresent()) {
		// person = optionalPerson.get();
		// }
		// if (!optionalPerson.isEmpty()) {
		// person = optionalPerson.get();
		// }
		// System.out.println(person);
		// repository.findById(1L).ifPresent(person -> System.out.println(person));
		// repository.findOne(1L).ifPresent(System.out::println);
		repository.findByNameContaining("ri").ifPresent(System.out::println);
	}

	@Transactional(readOnly = true)
	public void list() {
		// List<Person> persons = (List<Person>) repository.findAll();
		// List<Person> persons = (List<Person>)
		// repository.buscarByProgrammingLanguage("Java", "Andres");

		List<Person> persons = (List<Person>) repository.findByProgrammingLanguageAndName("Java", "Andres");
		persons.stream().forEach(person -> System.out.println(person));

		List<Object[]> personsValues = repository.obtenerPersonDataByProgrammingLanguage("Java");
		personsValues.stream().forEach(person -> {
			System.out.println(person[0] + " es experto en " + person[1]);
		});
	}

}
