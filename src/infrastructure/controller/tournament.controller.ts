import { Controller, Delete, Get, Query } from "@nestjs/common";
import { TournamentPagePresenter } from "../presenter/tournament/page/tournament-page.presenter";

@Controller("/tournament")
export class TournamentController {
  constructor(private tournamentPresenter: TournamentPagePresenter) {}

  @Get("/tournamentById")
  async getTournamentById(@Query('id') id: string) {
    return this.tournamentPresenter.getTournamentById(id);
  }

  @Get("/allTournament")
  async getAllTournament() {
    return {
      "status": "ok",
      "message": "Il servizio è attivo e funzionante correttamente.",
      "timestamp": event.toString()
    };
  }

  @Delete("/tournamentById")
  async deleteTournament(@Query('id') id: string) {
    return this.deleteTournament(id);
  }
}
