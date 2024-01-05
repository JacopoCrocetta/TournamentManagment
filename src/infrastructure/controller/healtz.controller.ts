import { Controller, Get, HttpCode, Logger } from "@nestjs/common";

@Controller()
export class AppController {

  @Get("/healthz")
  @HttpCode(200)
  getHealtz(): any {
    Logger.log("REACHING THE HEALTZ API...");

    const event = new Date();
    Logger.log("ALL IS UP AND ONLINE");
    return {
      "status": "ok",
      "message": "Il servizio è attivo e funzionante correttamente.",
      "timestamp": event.toString()
    };
  }
}
