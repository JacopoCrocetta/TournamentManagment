import { Controller, Delete, Get, Query } from "@nestjs/common";
import { TournamentPagePresenter } from "../presenter/tournament/page/tournament-page.presenter";

@Controller("/tournament")
export class TournamentController {
  constructor(private tournamentPresenter: TournamentPagePresenter) {}

  @Get("/tournamentById")
  async getTournamentById(@Query('id') id: number) {
    return this.tournamentPresenter.getTournamentById(id);
  }

  @Get("/allTournament")
  async getAllTournament() {
    return this.tournamentPresenter.getAllTournament()
  }

  @Delete("/tournamentById")
  async deleteTournament(@Query('id') id: number) {
    return this.deleteTournament(id);
  }
}
