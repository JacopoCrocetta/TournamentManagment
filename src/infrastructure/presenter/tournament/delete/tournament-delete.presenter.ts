import { Logger } from "@nestjs/common";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

export class TournamentDeletePresenter {
    constructor(private repository: ITournamentsRepository){}

    async deleteTournamentById(id: number): Promise<boolean>{
        Logger.log("[Application] TRYING TO DELETE THE TOURNAMENT WITH ID " + id);
        return await this.repository.delete(id);
    }
}