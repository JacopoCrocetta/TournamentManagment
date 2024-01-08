import { Controller, Query } from "@nestjs/common";
import { TournamentPagePresenter } from "../presenter/tournament/page/tournament-page.presenter";

@Controller("/tournament")
export class TournamentController {
  constructor(private tournamentPresenter: TournamentPagePresenter) {}

  async getTournamentById(@Query('id') id: number) {
    return this.tournamentPresenter.getTournamentById(id);
  }
}
