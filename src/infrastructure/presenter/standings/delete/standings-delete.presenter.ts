import { Logger } from "@nestjs/common";
import { IStandingRepository } from "src/core/repository/standings.repository";

export class StandingsDeletePresenter {
    constructor(private repository: IStandingRepository){}

    async deleteStandingById(id: number): Promise<boolean>{
        Logger.log("TRYING TO DELETE THE STANDINGS WITH ID " + id);
        return await this.repository.delete(id);
    }
}