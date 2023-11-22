import { Inject, Injectable, Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

@Injectable()
export class TournamentUpdate {
  constructor(@Inject() private repository: ITournamentsRepository) {}

  async updateTournament(inputToUpdate: Tournament): Promise<Tournament> {
    Logger.log("UPDATING TOURNAMENT WITH NAME " + inputToUpdate.tournamentName + "...");
    const tournamentUpdated = this.repository.update(inputToUpdate);

    Logger.log(inputToUpdate.tournamentName + " UPDATED");
    return tournamentUpdated;
  }
}
