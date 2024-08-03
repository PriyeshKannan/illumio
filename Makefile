ROOTDIR=$(PWD)
USE_DOCKER ?= true

.PHONY: init
init:
	@docker pull gradle:8.9.0-jdk11

.PHONY: test
test:
	@echo "[INFO[ Executing unit tests"
ifeq ($(USE_DOCKER),true)
	@echo "[INFO] Running in docker container ..."
	@docker run --rm -v "$(ROOTDIR)":/app/illumio -w /app/illumio gradle:8.9.0-jdk11 gradle test
else
	@./gradlew test
endif
	@echo "[INFO] Test result can be found in app/build folder..."


.PHONY: codecoverage
codecoverage:
	@echo "[INFO] Executing Code coverage"
ifeq ($(USE_DOCKER),true)
	@echo "[INFO] Running in docker container ..."
	@docker run --rm -v "$(ROOTDIR)":/app/illumio -w /app/illumio gradle:8.9.0-jdk11 gradle test jacocoTestCoverageVerification jacocoTestReport
else
	@./gradlew test jacocoTestCoverageVerification jacocoTestReport
endif
	@echo "[INFO] Jacco report can be found in app/build folder..."


.PHONY: run
run:
	@echo "[INFO] Run the application"
ifeq ($(USE_DOCKER),true)
	@echo "[INFO] Running in docker container ..."
	@docker run --rm -v "$(ROOTDIR)":/app/illumio -w /app/illumio gradle:8.9.0-jdk11 gradle run
else
	@./gradlew run
endif
