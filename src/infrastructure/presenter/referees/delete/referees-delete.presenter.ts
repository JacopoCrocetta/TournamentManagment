import { Logger } from "@nestjs/common";
import { IRefereesRepository } from "src/core/repository/referees.repository";

export class RefereesDeletePresenter {
    constructor(private repository: IRefereesRepository){}

    async deleteTournamentById(id: number): Promise<boolean>{
        Logger.log("TRYING TO DELETE THE REFEREES WITH ID " + id);
        return await this.repository.delete(id);
    }
}