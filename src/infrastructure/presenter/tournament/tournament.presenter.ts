import { Inject, Injectable } from "@nestjs/common";
import { TournamentPagePresenter } from "./page/tournament-page.presenter";
import { Tournament } from "src/core/entity/abstract-tournament";

@Injectable()
export class TournamentPresenter {
  private constructor(
    @Inject() private tournamentPagePresenter: TournamentPagePresenter
  ) {}

  async get(id: number): Promise<Tournament> {
    return this.tournamentPagePresenter.getTournamentById(id);
  }
}
