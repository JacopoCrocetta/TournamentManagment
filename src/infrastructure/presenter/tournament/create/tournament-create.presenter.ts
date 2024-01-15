import { Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";


export class TournamentCreatePresenter {
    constructor(private repository: ITournamentsRepository){}

    async createTournamentById(newTournament: Tournament){
        Logger.log("[Application] TRYING TO CREATE A NEW TOURNAMENT");
        return await this.repository.create(newTournament);
    }
}