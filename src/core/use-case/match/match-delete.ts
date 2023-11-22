import { Inject, Injectable, Logger } from "@nestjs/common";
import { IMatchRepository } from "src/core/repository/match.repository";

@Injectable()
export class MatchDelete {
  constructor(@Inject() private repository: IMatchRepository) {}

  delete = async (id: number): Promise<boolean> => {
    Logger.log("DELETING ITEM WITH ID " + id + " ...");
    this.repository.delete(id);

    Logger.log("CHECK IF THE ITEM WAS DELETED CORRECTLY...");
    const res = this.repository.getById(id) === null;

    return !!res;
  };
}
