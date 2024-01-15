import { Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

export class TournamentUpdatePresenter {
    constructor(private repository: ITournamentsRepository){}

    async updateTournamentById(tournamentToUpdate: Tournament):Promise<Tournament>{
        Logger.log("[Application] TRYING TO UPDATE THE TOURNAMENT WITH ID " + tournamentToUpdate.id);

        const tournamentUpdated = await this.repository.update(tournamentToUpdate);

        Logger.log("[Application] TOURNAMENT WITH ID " + tournamentUpdated.id+ " IS UPDATED");
        return tournamentUpdated;
    }
}