package com.sunnyhith.reactiveprogramming.learningreactivespring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

@SpringBootTest
class LearningreactivespringApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void fluxtest(){
		Flux<String> flux = Flux.just("A", "B", "C");
		flux.subscribe(System.out::println);
	}

	@Test
	void fluxtest2(){
		Flux.just("A", "B", "C")
				.log()
				.subscribe(System.out::println);
	}

	@Test
	void fluxtestWithError(){
		Flux.just("A", "B", "C")
				.concatWith(Flux.error(new RuntimeException("Some error occured")))
				.log()
				.subscribe(System.out::println, System.out::println,null);
	}

	@Test
	void fluxtestWithRange(){
		Flux.range(5,6)
				.log()
				.subscribe(System.out::println, System.out::println,null);
	}

	@Test
	void fluxtestWithInterval() throws InterruptedException {
		Flux.interval(Duration.ofSeconds(1))
				.log()
				.subscribe(System.out::println, System.out::println,null);
		Thread.	sleep(5000);
		//When a request of infinity is done request won't run on mail thread
	}

	@Test
	void fluxtestWithTake() throws InterruptedException {
		Flux.interval(Duration.ofSeconds(1))
				.log()
				.take(3)
				.subscribe(System.out::println, System.out::println,null);
		Thread.	sleep(5000);
		//When a request of infinity is done request won't run on mail thread
	}

	@Test
	void fluxtestWithErrorHandling(){
		Flux.just("A", "B", "C")
				.concatWith(Flux.error(new RuntimeException("Some error occured")))
				.onErrorReturn("Some item on exception")
				.log()
				.subscribe(System.out::println);
	}

	@Test
	void fluxtestWithFilter(){
		var cities = Arrays.asList("Kolkata","Pune","Mumbai","Jaipur","Delhi");
		var fluxcities = Flux.fromIterable(cities);
		var filteredCities = fluxcities.filter(city-> city.length() > 6);

		StepVerifier.create(filteredCities.log())
				.expectNext("Kolkata")
				.verifyComplete();

	}

	@Test
	void fluxtestWithMap(){
		Flux.range(1,5).map(data->data*data).subscribe(System.out::println);
	}

	@Test
	void fluxtestWithMapfilter(){
		Flux.range(1,5).map(data->data*data).filter(data->data%2==0).subscribe(System.out::println);
	}

	@Test
	void fluxtestWithflatMap(){
		var employeeIds = Arrays.asList("1","2","3","4","5","6","7","8","9");
		Flux.fromIterable(employeeIds).flatMap(id -> getEmployeeDetails(id))
	}

	@Test
	void DoOnError(){
		var employeeIds = Arrays.asList("1","2","3","4","5","6","7","8","9");
		Flux.fromIterable(employeeIds).concatWith(Flux.error(RuntimeException::new))
				.doOnError(error -> System.out.println("Some error occured"+error));
		// DO on error will execute do on Error and exit the loop
	}

	@Test
	void OnErrorReturn(){
		var employeeIds = Arrays.asList("1","2","3","4","5","6","7","8","9");
		Flux.fromIterable(employeeIds).concatWith(Flux.error(RuntimeException::new))
				.onErrorReturn("Some error occured");

		// On error Return  will execute On Error Return  and resume the loop
	}

	@Test
	void OnErrorResume(){
		var employeeIds = Arrays.asList("1","2","3","4","5","6","7","8","9");
		Flux.fromIterable(employeeIds).concatWith(Flux.error(RuntimeException::new))
				.onErrorResume(e->Flux.just(e.getMessage()));
		// On error Return  will execute On Error Return  and resume the loop
	}

	@Test
	void OnErrorMap(){
		var employeeIds = Arrays.asList("1","2","3","4","5","6","7","8","9");
		Flux.fromIterable(employeeIds).concatWith(Flux.error(new RuntimeException("Business exception")))
				.onErrorMap(e-> {
					if (e.getMessage().equals("Business exception")) {
						return new CustomException("Incorrect data");
					}
					return e;
				});
		// On error Return  will execute On Error Return  and resume the loop

	}

	class  CustomException extends Exception{

		public CustomException(Throwable incorrect_data) {
		}

		public void IncorrectException(){
			throw s;
		}
	}










}
