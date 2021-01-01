package com.system.event.platform;

import com.system.event.platform.dto.SerieMapper;
import com.system.event.platform.entities.*;
import com.system.event.platform.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * @author Mark Anthony Ortiz - ortizmark905@gmail.com
 */
@Slf4j
@SpringBootApplication
public class EventPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventPlatformApplication.class, args);
	}

	/*@Bean
	@Profile("dev")
	ServletRegistrationBean h2servletRegistration(){
		ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
	}*/

	@Bean
	@Profile({"dev", "test"})
	public CommandLineRunner executeQuery(EventService eventService,
										  CategoryService categoryService,
										  InformationService informationService,
										  EditionService editionService,
										  SerieService serieService, SerieMapper serieMapper) {
		return (args) -> {
			Event e1 = eventService.create("test1");
			Event e2 = eventService.create("test2");
			eventService.getAll().forEach(str -> log.info(str.getEventName()));
			eventService.getAll().forEach(str -> log.info(str.getEventName()));
			eventService.create("test3");
			eventService.getAll().forEach(str -> log.info(str.getEventName()));
			eventService.getAll().forEach(str -> log.info(str.getEventName()));


			Category cat = categoryService.create("cat1", "some category");
			Information info = informationService.create(Information.builder().city("Mati City").build());
			Edition edition = editionService.create("2021", System.currentTimeMillis(), System.currentTimeMillis(), true, e1);
			Serie serie1 = serieService.create("Serie1", Gender.Male, 100, 15, 100, e1);


			serie1 = serie1.toBuilder().edition(edition).build();
			serie1 = serieService.updateSerie(serieMapper.toDto(serie1), serieMapper::updateEntity);
			edition = editionService.getEditionAndRacesAndSeries(edition);

			e2 = e2.toBuilder().category(cat).build();

			e1 = e1.toBuilder().category(cat).information(info).build();

			eventService.update(e1);
			eventService.update(e2);
			List<Event> e3 = categoryService.getCategoryEvents(cat.getId());

			for (Event event : e3) {
				System.out.println(event.getEventName());
			}

			Event eInfo = eventService.getEvent(e1.getId());
			eInfo = eventService.getEventAndEditions(e1);
			System.out.println(eInfo.getInformation().getCity());

			eInfo = eventService.getEventAndSeries(eInfo);

			eInfo.getEditions().forEach(System.out::println);
			eInfo.getSeries().forEach(System.out::println);

			edition.getSeries().forEach(System.out::println);
			edition.getRaces().forEach(System.out::println);
		};
	}

}
