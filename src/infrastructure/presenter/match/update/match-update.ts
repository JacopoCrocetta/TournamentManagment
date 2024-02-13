import { Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

export class MatchUpdatePresenter {
    constructor(private repository: ITournamentsRepository){}

    async updateMatchById(tournamentToUpdate: Tournament):Promise<Tournament>{
        Logger.log("TRYING TO UPDATE THE MATCH WITH ID " + tournamentToUpdate.id);

        const tournamentUpdated = await this.repository.update(tournamentToUpdate);

        Logger.log("MATCH WITH ID " + tournamentUpdated.id+ " IS UPDATED");
        return tournamentUpdated;
    }
}