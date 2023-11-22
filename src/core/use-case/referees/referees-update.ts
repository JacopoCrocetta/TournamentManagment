import { Inject, Injectable, Logger } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { IRefereesRepository } from "src/core/repository/referees.repository";

@Injectable()
export class RefereesUpdate {
  constructor(@Inject() private repository: IRefereesRepository) {}

  async updateTournament(inputToUpdate: Referees): Promise<Referees> {
    Logger.log("UPDATING REFEREES " + inputToUpdate.nome + "...");
    const tournamentUpdated = this.repository.update(inputToUpdate);

    Logger.log(inputToUpdate.nome + " UPDATED");
    return tournamentUpdated;
  }
}
