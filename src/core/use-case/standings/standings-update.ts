import { Inject, Injectable, Logger } from "@nestjs/common";
import { Standings } from "src/core/entity/abstract-standings";
import { Tournament } from "src/core/entity/abstract-tournament";
import { IStandingRepository } from "src/core/repository/standings.repository";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

@Injectable()
export class StandingsUpdate {
  constructor(@Inject() private repository: IStandingRepository) {}

  async updateTournament(inputToUpdate: Standings): Promise<Standings> {
    Logger.log("UPDATING STANDINGS WITH ID " + inputToUpdate.id + "...");
    const standingsUpdate = this.repository.update(inputToUpdate);

    Logger.log(inputToUpdate.id + " UPDATED");
    return standingsUpdate;
  }
}
