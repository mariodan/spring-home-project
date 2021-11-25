package ro.home.project.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.home.project.model.request.FilePaymentRq;
import ro.home.project.model.response.FilePaymentsRs;
import ro.home.project.service.FilePaymentsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class FilePaymentsController {

	private final FilePaymentsService filePaymentsService;

	@GetMapping
	public List<FilePaymentsRs> getFilePayments(@RequestParam String leCif) {
		return filePaymentsService.getFilePaymentsByLeCif(leCif);
	}

	@GetMapping("/{id}")
	public FilePaymentsRs getFilePaymentById(@PathVariable long id) {
		return filePaymentsService.getFilePaymentById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteFilePaymentById(@PathVariable long id) {
		filePaymentsService.deleteFilePayment(List.of(id));
		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public FilePaymentsRs createFilePayment(@Valid @RequestBody FilePaymentRq filePaymentRq) {
		return filePaymentsService.createFilePayment(filePaymentRq);
	}

	@PutMapping("/{id}")
	public FilePaymentsRs updateFilePayment(@PathVariable long id, @RequestBody FilePaymentRq filePaymentRq) {
		return filePaymentsService.updateEntity(id, filePaymentRq);
	}
}
