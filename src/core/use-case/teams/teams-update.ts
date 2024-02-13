import { Inject, Injectable, Logger } from "@nestjs/common";
import { Teams } from "src/core/entity/abstract-teams";
import { ITeamsRepository } from "src/core/repository/teams.repository";

@Injectable()
export class TeamUpdate {
  constructor(@Inject() private repository: ITeamsRepository) {}

  async updateTournament(inputToUpdate: Teams): Promise<Teams> {
    Logger.log(
      "UPDATING TOURNAMENT WITH NAME " + inputToUpdate.squadName + "..."
    );
    const tournamentUpdated = this.repository.update(inputToUpdate);

    Logger.log(inputToUpdate.squadName + " UPDATED");
    return tournamentUpdated;
  }
}
