import { Logger } from "@nestjs/common";
import { IMatchRepository } from "src/core/repository/match.repository";

export class MatchDeletePresenter {
    constructor(private repository: IMatchRepository){}

    async deleteMatchById(id: number): Promise<boolean>{
        Logger.log("TRYING TO DELETE THE MATCH WITH ID " + id);
        return await this.repository.delete(id);
    }
}