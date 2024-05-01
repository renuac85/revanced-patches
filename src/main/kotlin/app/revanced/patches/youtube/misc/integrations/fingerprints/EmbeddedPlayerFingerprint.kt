package app.revanced.patches.youtube.misc.integrations.fingerprints

import app.revanced.patches.shared.misc.integrations.integrationsHook
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * For embedded playback inside the Google app (such as the in-app 'discover' tab).
 *
 * Note: this fingerprint may or may not be needed, as
 * [remoteEmbedFragmentFingerprint] might be set before this is called.
 */
internal val embeddedPlayerFingerprint = integrationsHook(
    // Integrations context is the third method parameter.
    contextRegisterResolver = { it.implementation!!.registerCount - it.parameters.size },
) {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.STATIC)
    returns("V")
    parameters("Landroid/content/Context;", "L", "L")
    strings("android.hardware.type.television") // String is also found in other classes
    custom { methodDef, _ ->
        methodDef.definingClass == "Lcom/google/android/apps/youtube/embeddedplayer/EmbeddedPlayer;" &&
            methodDef.name == "initialize"
    }
}
